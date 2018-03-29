/*
 * Copyright 2018 Alidibir Akhbulatov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akhbulatov.wordkeeper.ui.listener;

import android.support.annotation.StringRes;

/**
 * @author Alidibir Akhbulatov
 * @since 04.10.2016
 */
public interface FabAddWordListener {
    /**
     * Called when clicks on FAB to add a word
     *
     * @param titleId        The ID of the title of the dialog
     * @param positiveTextId The ID of the text on the positive button of the dialog
     * @param negativeTextId The ID of the text on the negative button of the dialog
     */
    void onFabAddWordClick(@StringRes int titleId,
                           @StringRes int positiveTextId,
                           @StringRes int negativeTextId);
}
