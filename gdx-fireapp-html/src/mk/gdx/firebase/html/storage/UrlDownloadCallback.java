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

package mk.gdx.firebase.html.storage;

import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.TypedArrays;
import com.google.gwt.typedarrays.shared.Uint8Array;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;

import mk.gdx.firebase.callbacks.DownloadCallback;

/**
 * Provides access to download url of firebase storage file.
 */
public class UrlDownloadCallback implements DownloadCallback<String> {

    private DownloadCallback originalCallback;

    public UrlDownloadCallback(DownloadCallback originalCallback) {
        this.originalCallback = originalCallback;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onSuccess(String downloadUrl) {
        xmlHttpRequest(downloadUrl);
    }

    /**
     * @param e Exception describes what was wrong during authorization.
     */
    @Override
    public void onFail(Exception e) {
        originalCallback.onFail(e);
    }

    @SuppressWarnings("unchecked")
    private void xmlHttpRequest(String downloadUrl) {
        final XMLHttpRequest xmlHttpRequest = XMLHttpRequest.create();
        xmlHttpRequest.setResponseType(XMLHttpRequest.ResponseType.ArrayBuffer);
        String[] urlParts = downloadUrl.split("token=");
        if (urlParts.length < 2)
            throw new IllegalStateException("Download url should contains token variable.");
        String accessToken = urlParts[1];
        xmlHttpRequest.setOnReadyStateChange(new ReadyStateChangeHandler() {
            @Override
            public void onReadyStateChange(XMLHttpRequest xhr) {
                if (xhr.getReadyState() == XMLHttpRequest.DONE &&
                        (xhr.getStatus() >= 200 && xhr.getStatus() < 300)) {
                    ArrayBuffer arrayBuffer = xhr.getResponseArrayBuffer();
                    Uint8Array array = TypedArrays.createUint8Array(arrayBuffer);
                    byte[] buffer = new byte[array.length()];
                    for (int i = 0; i < array.length(); i++)
                        buffer[i] = (byte) array.get(i);
                    originalCallback.onSuccess(buffer);
                } else if (xhr.getReadyState() == XMLHttpRequest.DONE) {
                    originalCallback.onFail(new Exception("XHR error, status code: " + xhr.getStatus()));
                }
            }
        });
        xmlHttpRequest.open("GET", downloadUrl);
        xmlHttpRequest.setRequestHeader("Authorization", "Bearer " + accessToken);
        xmlHttpRequest.send();
    }
}
