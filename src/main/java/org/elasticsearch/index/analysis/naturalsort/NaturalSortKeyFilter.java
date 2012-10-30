/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.analysis.naturalsort;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.IndexableBinaryStringTools;

import java.io.IOException;
import java.text.Collator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class NaturalSortKeyFilter extends TokenFilter {
  private final Collator collator;
  private final int digits;
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

  /**
   * @param input Source token stream
   * @param collator CollationKey generator
   * @param digits the maximum number of digits for natural sort length, default is 1 (i.e. up to number lengths of 9 can be sorted) 
   */
  public NaturalSortKeyFilter(TokenStream input, Collator collator, int digits) {
    super(input);
    this.collator = (Collator) collator.clone();
    this.digits = digits;
  }

  @Override
  public boolean incrementToken() throws IOException {
    if (input.incrementToken()) {
      byte[] collationKey = collator.getCollationKey(natural(termAtt.toString())).toByteArray();
      int encodedLength = IndexableBinaryStringTools.getEncodedLength(
          collationKey, 0, collationKey.length);
      termAtt.resizeBuffer(encodedLength);
      termAtt.setLength(encodedLength);
      IndexableBinaryStringTools.encode(collationKey, 0, collationKey.length,
          termAtt.buffer(), 0, encodedLength);
      return true;
    } else {
      return false;
    }
  }
  
  private final static Pattern numberPattern = Pattern.compile("(\\+|\\-)?([0-9]+)");
  
  private String natural(String s) {
      StringBuffer sb = new StringBuffer();
      Matcher m = numberPattern.matcher(s);
      if (m.find()) {
	  int len = m.group(2).length();
	  String repl = String.format("%0"+digits+"d", len) + m.group();
	  m.appendReplacement(sb, repl);
      }
      m.appendTail(sb);
      return sb.toString();
  }
}
