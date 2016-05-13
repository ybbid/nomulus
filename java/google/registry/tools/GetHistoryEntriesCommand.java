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

import static com.google.domain.registry.model.ofy.ObjectifyService.ofy;
import static com.google.domain.registry.util.DateTimeUtils.END_OF_TIME;
import static com.google.domain.registry.util.DateTimeUtils.START_OF_TIME;

import com.google.domain.registry.model.reporting.HistoryEntry;
import com.google.domain.registry.tools.Command.GtechCommand;
import com.google.domain.registry.tools.Command.RemoteApiCommand;
import com.google.domain.registry.xml.XmlTransformer;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.joda.time.DateTime;

/** Command to show history entries. */
@Parameters(separators = " =",
  commandDescription = "Show history entries that occurred in a given time range")
final class GetHistoryEntriesCommand implements RemoteApiCommand, GtechCommand {

  @Parameter(
      names = {"-a", "--after"},
      description = "Only show history entries that occurred at or after this time")
  private DateTime after = START_OF_TIME;

  @Parameter(
      names = {"-b", "--before"},
      description = "Only show history entries that occurred at or before this time")
  private DateTime before = END_OF_TIME;

  @Override
  public void run() {
    for (HistoryEntry entry : ofy().load().type(HistoryEntry.class)
        .order("modificationTime")
        .filter("modificationTime >=", after)
        .filter("modificationTime <=", before)) {
      System.out.printf(
          "Client: %s\nTime: %s\nClient TRID: %s\nServer TRID: %s\n%s\n",
          entry.getClientId(),
          entry.getModificationTime(),
          entry.getTrid().getClientTransactionId(),
          entry.getTrid().getServerTransactionId(),
          entry.getXmlBytes() == null
              ? String.format("[no XML stored for %s]\n", entry.getType())
              : XmlTransformer.prettyPrint(entry.getXmlBytes()));
    }
  }
}