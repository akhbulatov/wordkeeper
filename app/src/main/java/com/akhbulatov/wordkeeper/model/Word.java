/*
 * Copyright 2016 Alidibir Akhbulatov <alidibir.akhbulatov@gmail.com>
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

package com.akhbulatov.wordkeeper.model;

/**
 * @author Alidibir Akhbulatov
 * @since 26.11.2016
 */
public class Word {

    private long mId;
    private String mName;
    private String mTranslation;
    private String mCategory;

    public Word() {
    }

    public Word(long id) {
        mId = id;
    }

    public Word(String name, String translation, String category) {
        mName = name;
        mTranslation = translation;
        mCategory = category;
    }

    public Word(long id, String name, String translation, String category) {
        mId = id;
        mName = name;
        mTranslation = translation;
        mCategory = category;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTranslation() {
        return mTranslation;
    }

    public void setTranslation(String translation) {
        mTranslation = translation;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
}
