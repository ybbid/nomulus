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

package com.google.domain.registry.request;

import static com.google.common.net.MediaType.PLAIN_TEXT_UTF_8;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.domain.registry.testing.ExceptionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

/** Unit tests for {@link ResponseImpl}. */
@RunWith(MockitoJUnitRunner.class)
public class ResponseImplTest {

  @Rule
  public final ExceptionRule thrown = new ExceptionRule();

  @Mock
  private HttpServletResponse rsp;

  @Test
  public void testSetStatus() throws Exception {
    new ResponseImpl(rsp).setStatus(666);
    verify(rsp).setStatus(666);
    verifyNoMoreInteractions(rsp);
  }

  @Test
  public void testSetContentType() throws Exception {
    new ResponseImpl(rsp).setContentType(PLAIN_TEXT_UTF_8);
    verify(rsp).setContentType("text/plain; charset=utf-8");
    verifyNoMoreInteractions(rsp);
  }

  @Test
  public void testSetPayload() throws Exception {
    StringWriter httpOutput = new StringWriter();
    when(rsp.getWriter()).thenReturn(new PrintWriter(httpOutput));
    new ResponseImpl(rsp).setPayload("hello world");
    assertThat(httpOutput.toString()).isEqualTo("hello world");
  }

  @Test
  public void testSendJavaScriptRedirect_producesHtmlScript() throws Exception {
    StringWriter httpOutput = new StringWriter();
    when(rsp.getWriter()).thenReturn(new PrintWriter(httpOutput));
    new ResponseImpl(rsp).sendJavaScriptRedirect("/hello");
    assertThat(httpOutput.toString()).isEqualTo(
        "<script>window.location.replace(\"/hello\");</script><a href=\"/hello\">/hello</a>");
  }
}