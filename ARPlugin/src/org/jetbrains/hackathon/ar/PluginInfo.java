/*
 * Copyright (C) 2009-2011 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.jetbrains.hackathon.ar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.maxmpz.poweramp.player.RemoteTrackTime;
import org.geometerplus.android.fbreader.api.PluginApi;

import java.util.ArrayList;
import java.util.List;

public class PluginInfo extends PluginApi.PluginInfo {
    private RemoteTrackTime mRemoteTrackTime;



    @Override
    protected List<PluginApi.ActionInfo> implementedActions(Context context) {
        System.out.println("started");

        Intent intent = new Intent(context, PowerampListener.class);
//        ComponentName componentName = context.startService(intent);
//        System.out.println("service " + componentName);


        List<PluginApi.ActionInfo> actions = new ArrayList<PluginApi.ActionInfo>(2);
        actions.add(new PluginApi.MenuActionInfo(
                Uri.parse("http://data.ar.org/plugin/ar/navigate/to/audio"),
                "Navigate to audio",
                Integer.MAX_VALUE
        ));
        actions.add(new PluginApi.MenuActionInfo(
                Uri.parse("http://data.ar.org/plugin/ar/navigate/from/audio"),
                "Navigate from audio",
                Integer.MAX_VALUE
        ));

        return actions;
    }
}
