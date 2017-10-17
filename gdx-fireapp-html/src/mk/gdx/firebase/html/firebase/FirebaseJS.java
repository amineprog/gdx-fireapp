/*
 * Copyright 2017 mk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mk.gdx.firebase.html.firebase;

import com.badlogic.gdx.Gdx;

/**
 * Provides javascript calls for firebase.js loading.
 */
public class FirebaseJS
{
    public static native void initializeFirebase(String initializationScript) /*-{
        console.log("GdxFireapp: eval initialization script...");
        eval(initializationScript);
        @mk.gdx.firebase.html.firebase.FirebaseJS::setFirebaseScriptIsLoaded()();
    }-*/;

    public static native boolean isFirebaseLoaded() /*-{
        return (typeof $wnd.firebase != 'undefined');
    }-*/;

    public static void setFirebaseScriptIsLoaded()
    {
        Gdx.app.log("GdxFireapp", "firebase.js just loaded.");
        FirebaseScriptInformant.setIsLoaded(true);
    }
}