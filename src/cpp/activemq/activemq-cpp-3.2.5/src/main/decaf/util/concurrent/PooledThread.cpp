/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#include <decaf/util/concurrent/PooledThread.h>
#include <decaf/util/concurrent/ThreadPool.h>
#include <decaf/util/concurrent/TaskListener.h>
#include <decaf/lang/exceptions/IllegalArgumentException.h>

#include <iostream>

using namespace decaf;
using namespace decaf::lang;
using namespace decaf::lang::exceptions;
using namespace decaf::util;
using namespace decaf::util::concurrent;

////////////////////////////////////////////////////////////////////////////////
LOGDECAF_INITIALIZE(logger, PooledThread, "com.activemq.concurrent.PooledThread")

////////////////////////////////////////////////////////////////////////////////
PooledThread::PooledThread(ThreadPool* pool)
{
    if(pool == NULL)
    {
        throw IllegalArgumentException( __FILE__, __LINE__,
            "PooledThread::PooledThread");
    }

    busy = false;
    done = false;

    listener = NULL;

    // Store our Pool.
    this->pool = pool;
}

////////////////////////////////////////////////////////////////////////////////
PooledThread::~PooledThread()
{
}

////////////////////////////////////////////////////////////////////////////////
void PooledThread::run()
{
    ThreadPool::Task task;

    try
    {
        while(!done)
        {
            //LOGCMS_DEBUG(logger, "PooledThread::run - Entering deQ");

            // Blocks until there something to be done
            task = pool->deQueueTask();

            //LOGCMS_DEBUG(logger, "PooledThread::run - Exited deQ");

            // Check if the Done Flag is set, in case it happened while we
            // were waiting for a task
            if(done)
            {
                break;
            }

            // If we got here and the runnable was null then something
            // bad must have happened.  Throw an Exception and bail.
            if(!task.first)
            {
                throw Exception( __FILE__, __LINE__,
                    "PooledThread::run - Retrieive NULL task from Pool.");
            }

            // Got some work to do, so set flag to busy
            busy = true;

            // Inform a listener that we are going to start
            if(listener)
            {
                /*LOGCMS_DEBUG(logger,
                   "PooledThread::run - Inform Listener we are starting");*/
                listener->onTaskStarted(this);
            }

            // Perform the work
            task.first->run();

            /*LOGCMS_DEBUG(logger,
                "PooledThread::run - Inform Task Listener we are done");*/

            // Notify the Task listener that we are done
            task.second->onTaskComplete(task.first);

            // Inform a listener that we are going to stop and wait
            // for a new task
            if(listener)
            {
                /*LOGCMS_DEBUG(logger,
                    "PooledThread::run - Inform Listener we are done");*/
                listener->onTaskCompleted(this);
            }

            // Set flag to inactive, we will wait for work
            busy = false;
        }
    }
    catch( Exception& ex )
    {
        ex.setMark( __FILE__, __LINE__ );

        // Notify the Task owner
        if(task.first && task.second)
        {
            task.second->onTaskException(task.first, ex);
        }

        busy = false;

        // Notify the PooledThreadListener
        if(listener)
        {
            listener->onTaskException(this, ex);
        }
    }
    catch(...)
    {
        Exception ex(
            __FILE__, __LINE__,
            "PooledThread::run - Caught Unknown Exception");

        // Notify the Task owner
        if(task.first && task.second)
        {
            task.second->onTaskException(task.first, ex);
        }

        busy = false;

        // Notify the PooledThreadListener
        if(listener)
        {
            listener->onTaskException(this, ex);
        }
    }
}

////////////////////////////////////////////////////////////////////////////////
void PooledThread::stop() throw ( Exception )
{
    done = true;
}
