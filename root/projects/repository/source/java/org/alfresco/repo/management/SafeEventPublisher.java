/*
 * Copyright (C) 2005-2009 Alfresco Software Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

 * As a special exception to the terms and conditions of version 2.0 of 
 * the GPL, you may redistribute this Program in connection with Free/Libre 
 * and Open Source Software ("FLOSS") applications as described in Alfresco's 
 * FLOSS exception.  You should have received a copy of the text describing 
 * the FLOSS exception, and it is also available here: 
 * http://www.alfresco.com/legal/licensing"
 */
package org.alfresco.repo.management;

import java.util.LinkedList;
import java.util.List;

import org.alfresco.util.AbstractLifecycleBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * An event publisher that is safe to use while the context is in the process of refreshing. It queues up events until
 * the context has refreshed, after which point events are published in real time.
 * 
 * @author dward
 */
public class SafeEventPublisher extends AbstractLifecycleBean implements ApplicationEventPublisher
{

    /** Has the application started? */
    private boolean isApplicationStarted;

    /** The queued events. */
    private List<ApplicationEvent> queuedEvents = new LinkedList<ApplicationEvent>();

    /*
     * (non-Javadoc)
     * @see org.alfresco.util.AbstractLifecycleBean#onBootstrap(org.springframework.context.ApplicationEvent)
     */
    @Override
    protected void onBootstrap(ApplicationEvent event)
    {
        this.isApplicationStarted = true;
        for (ApplicationEvent queuedEvent : this.queuedEvents)
        {
            publishEvent(queuedEvent);
        }
        this.queuedEvents.clear();
    }

    /*
     * (non-Javadoc)
     * @see org.alfresco.util.AbstractLifecycleBean#onShutdown(org.springframework.context.ApplicationEvent)
     */
    @Override
    protected void onShutdown(ApplicationEvent event)
    {
        this.isApplicationStarted = false;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.springframework.context.ApplicationEventPublisher#publishEvent(org.springframework.context.ApplicationEvent)
     */
    public void publishEvent(ApplicationEvent event)
    {
        ApplicationContext context = getApplicationContext();
        if (this.isApplicationStarted)
        {
            context.publishEvent(event);
        }
        else
        {
            this.queuedEvents.add(event);
        }
    }

}
