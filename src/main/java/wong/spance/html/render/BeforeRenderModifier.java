package wong.spance.html.render;

/*
 * Copyright 2010-2011 Spance Wong.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import wong.spance.html.element.HtmlElement;

/**
 * Created by spance on 14/4/4.
 */
public abstract class BeforeRenderModifier extends Modifier {

    public BeforeRenderModifier(String pattern) {
        super(ModifierPoint.BEFORE, pattern);
    }

    @Override
    protected abstract Boolean handler(HtmlElement element, RenderContext context);
}
