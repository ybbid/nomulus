// Copyright 2016 The Domain Registry Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.domain.registry.tools;

import com.google.common.base.Joiner;
import com.google.domain.registry.tools.Command.GtechCommand;
import com.google.domain.registry.tools.Command.RemoteApiCommand;
import com.google.domain.registry.whois.Whois;
import com.google.domain.registry.whois.WhoisException;
import com.google.domain.registry.whois.WhoisResponse;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;

import javax.inject.Inject;

/** Command to execute a WHOIS query. */
@Parameters(separators = " =", commandDescription = "Manually perform a WHOIS query")
final class WhoisQueryCommand implements RemoteApiCommand, GtechCommand {

  @Parameter(
      description = "WHOIS query string",
      required = true)
  private List<String> mainParameters;

  @Parameter(
      names = "--unicode",
      description = "When set, output will be Unicode")
  private boolean unicode;

  @Inject
  Whois whois;

  @Override
  public void run() {
    WhoisResponse response;
    try {
      response = whois.lookup(Joiner.on(' ').join(mainParameters));
    } catch (WhoisException e) {
      response = e;
    }
    System.out.println(response.getPlainTextOutput(unicode));
  }
}