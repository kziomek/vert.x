/*
 * Copyright (c) 2011-2013 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.vertx.core.impl;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutorGroup;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;

/**
 * Extends to expose Netty interactions for reusing existing Netty codecs and benefit from the features
 * {@link io.vertx.core.net.NetServer} and {@link io.vertx.core.net.NetClient}:
 *
 * <ul>
 *   <li>Server sharing</li>
 *   <li>SSL/TLS</li>
 *   <li>SNI</li>
 *   <li>SSL/TLS upgrade</li>
 *   <li>Write batching during read operation</li>
 *   <li>Client proxy support</li>
 * </ul>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface NetSocketInternal extends NetSocket {

  /**
   * Returns the {@link ChannelHandlerContext} of the last handler (named {@code handler}) of the pipeline that
   * delivers message to the application with the {@link #handler(Handler)} and {@link #messageHandler(Handler)}.
   * <p/>
   * Handlers should be inserted in the pipeline using {@link io.netty.channel.ChannelPipeline#addBefore(String, String, ChannelHandler)}:
   * <p/>
   * <code><pre>
   *   ChannelPipeline pipeline = so.channelHandlerContext().pipeline();
   *   pipeline.addBefore("handler", "myhandler", new MyHandler());
   * </pre></code>
   * @return the channel handler context
   */
  ChannelHandlerContext channelHandlerContext();

  /**
   * Write a message in the channel pipeline.
   * <p/>
   * When a read operation is in progress, the flush operation is delayed until the read operation completes.
   *
   * @param message the message to write, it should be handled by one of the channel pipeline handlers
   * @return a reference to this, so the API can be used fluently
   */
  NetSocketInternal writeMessage(Object message);

  /**
   * Like {@link #writeMessage(Object)} but with an {@code handler} called when the message has been written
   * or failed to be written.
   */
  NetSocketInternal writeMessage(Object message, Handler<AsyncResult<Void>> handler);

  /**
   * Set a {@code handler} on this socket to process the messages produced by this socket. The message can be
   * {@link io.netty.buffer.ByteBuf} or other messages produced by channel pipeline handlers.
   * <p/>
   * The {@code} handler should take care of releasing pooled / direct messages.
   * <p/>
   * The handler replaces any {@link #handler(Handler)} previously set.
   *
   * @param handler the handler to set
   * @return a reference to this, so the API can be used fluently
   */
  NetSocketInternal messageHandler(Handler<Object> handler);

}
