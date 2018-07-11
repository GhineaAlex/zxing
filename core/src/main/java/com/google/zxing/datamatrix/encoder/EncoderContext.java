/*
 * Copyright 2006-2007 Jeremias Maerki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.datamatrix.encoder;

import java.io.UnsupportedEncodingException;

import com.google.zxing.Dimension;

final class EncoderContext {

   final String msg;
  private SymbolShapeHint shape = SymbolShapeHint.FORCE_NONE; //changed
  private Dimension minSize;
  private Dimension maxSize;
  private final StringBuilder codewords;
  int pos = 0;
  private int newEncoding = -1;
  private SymbolInfo symbolInfo;
  private int skipAtEnd = 0;
  private static final String DEFAULT_ASCII_ENCODING = "ISO-8859-1";
  private boolean gs1;

  EncoderContext(String msg) {
    //From this point on Strings are not Unicode anymore!
    byte[] msgBinary;
    try {
    	msgBinary = msg.getBytes(DEFAULT_ASCII_ENCODING);
    } catch(UnsupportedEncodingException e) {
    	throw new UnsupportedOperationException("Unsupported encoding" + e.getMessage());
    }
    StringBuilder sb = new StringBuilder(msgBinary.length);
    for (int i = 0, c = msgBinary.length; i < c; i++) {
      char ch = (char) (msgBinary[i] & 0xff);
      if (ch == '?' && msg.charAt(i) != '?') {
        throw new IllegalArgumentException("Message contains characters outside ISO-8859-1 encoding.");
      }

      sb.append(ch);
    }
    this.msg = sb.toString(); //Not Unicode here!

    this.codewords = new StringBuilder(msg.length());
  }

  public EncoderContext(byte[] data) {
	  StringBuilder sb = new StringBuilder(data.length);
	  for(int i = 0, c = data.length; i < c; i++) {
		  char ch = (char) (data[i] & 0xff);
		  sb.append(ch);
	  }
	  this.msg = sb.toString();
	  this.codewords = new StringBuilder(msg.length());

  }

  public void setSymbolShape(SymbolShapeHint shape) {
    this.shape = shape;
  }

  public void setSizeConstraints(Dimension minSize, Dimension maxSize) {
    this.minSize = minSize;
    this.maxSize = maxSize;
  }

  public String getMessage() {
    return this.msg;
  }

  public void setSkipAtEnd(int count) {
    this.skipAtEnd = count;
  }

  public char getCurrentChar() {
    return msg.charAt(pos);
  }

  public char getCurrent() {
    return msg.charAt(pos);
  }

  public StringBuilder getCodewords() {
    return codewords;
  }

  public void writeCodewords(String codewords) {
    this.codewords.append(codewords);
  }

  public void writeCodeword(char codeword) {
    this.codewords.append(codeword);
  }

  public int getCodewordCount() {
    return this.codewords.length();
  }

  public int getNewEncoding() {
    return newEncoding;
  }

  public void signalEncoderChange(int encoding) {
    this.newEncoding = encoding;
  }

  public void resetEncoderSignal() {
    this.newEncoding = -1;
  }

  public boolean hasMoreCharacters() {
    return pos < getTotalMessageCharCount();
  }

  private int getTotalMessageCharCount() {
    return msg.length() - skipAtEnd;
  }

  public int getRemainingCharacters() {
    return getTotalMessageCharCount() - pos;
  }

  public SymbolInfo getSymbolInfo() {
    return symbolInfo;
  }

  public void updateSymbolInfo() {
    updateSymbolInfo(getCodewordCount());
  }

  public void updateSymbolInfo(int len) {
    if (this.symbolInfo == null || len > this.symbolInfo.getDataCapacity()) {
      this.symbolInfo = SymbolInfo.lookup(len, shape, minSize, maxSize, true);
    }
  }

  public void resetSymbolInfo() {
    this.symbolInfo = null;
  }

  public boolean isGs1() {
	  return gs1;
  }
  public void setGs1(boolean gs1) {
	  this.gs1 = gs1;
  }
}
