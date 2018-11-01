/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.uast.kotlin

import com.intellij.psi.PsiSubstitutor
import com.intellij.psi.ResolveResult
import com.intellij.psi.infos.CandidateInfo
import org.jetbrains.kotlin.psi.KtSuperExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UIdentifier
import org.jetbrains.uast.UMultiResolvable
import org.jetbrains.uast.USuperExpression
import org.jetbrains.uast.kotlin.declarations.KotlinUIdentifier

class KotlinUSuperExpression(
        override val psi: KtSuperExpression,
        givenParent: UElement?
) : KotlinAbstractUExpression(givenParent), USuperExpression, UMultiResolvable, KotlinUElementWithType, KotlinEvaluatableUElement {
    override val label: String?
        get() = psi.getLabelName()

    override val labelIdentifier: UIdentifier?
        get() = psi.getTargetLabel()?.let { KotlinUIdentifier(it, this) }

    override fun resolve() = psi.analyze()[BindingContext.LABEL_TARGET, psi.getTargetLabel()]

    override fun multiResolve(): Iterable<ResolveResult> = listOfNotNull(resolve()?.let { CandidateInfo(it, PsiSubstitutor.EMPTY) })
}