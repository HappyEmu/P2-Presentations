/*****************************************************************************
 * Copyright (C) Codehaus.org                                                *
 * ------------------------------------------------------------------------- *
 * Licensed under the Apache License, Version 2.0 (the "License");           *
 * you may not use this file except in compliance with the License.          *
 * You may obtain a copy of the License at                                   *
 *                                                                           *
 * http://www.apache.org/licenses/LICENSE-2.0                                *
 *                                                                           *
 * Unless required by applicable law or agreed to in writing, software       *
 * distributed under the License is distributed on an "AS IS" BASIS,         *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 * See the License for the specific language governing permissions and       *
 * limitations under the License.                                            *
 *****************************************************************************/
package org.codehaus.jparsec;

import java.util.List;

/**
 * A parser that ignores delimiters before each repetitive pattern until no
 * more delimiter is found.
 *
 * @author Ben Yu
 */
final class DelimitedListParser<T> extends DelimitedParser<T, List<T>> {
  private final ListFactory<T> listFactory;

  DelimitedListParser(Parser<T> p, Parser<?> delim, ListFactory<T> listFactory) {
    super(p, delim);
    this.listFactory = listFactory;
  }

  @Override List<T> begin() {
    return listFactory.newList();
  }

  @Override void element(ParseContext ctxt, List<T> list) {
    list.add(parser.getReturn(ctxt));
  }
}