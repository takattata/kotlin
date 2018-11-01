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

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiSubstitutor
import com.intellij.psi.ResolveResult
import com.intellij.psi.infos.CandidateInfo
import org.jetbrains.kotlin.psi.KtDestructuringDeclarationEntry
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.uast.*

class KotlinUQualifiedReferenceExpression(
        override val psi: KtDotQualifiedExpression,
        givenParent: UElement?
) : KotlinAbstractUExpression(givenParent), UQualifiedReferenceExpression, UMultiResolvable,
        KotlinUElementWithType, KotlinEvaluatableUElement {
    override val receiver by lz { KotlinConverter.convertOrEmpty(psi.receiverExpression, this) }
    override val selector by lz { KotlinConverter.convertOrEmpty(psi.selectorExpression, this) }
    override val accessType = UastQualifiedExpressionAccessType.SIMPLE
    
    override fun resolve() = psi.selectorExpression?.resolveCallToDeclaration(this)

    override val resolvedName: String?
        get() = (resolve() as? PsiNamedElement)?.name

    override fun multiResolve(): Iterable<ResolveResult> = psi.selectorExpression?.let { ktExpression ->
        getReferenceVariants(ktExpression, ktExpression.name ?: ktExpression.text)
            .mapNotNull { descriptor ->
                descriptor.toSource()?.getMaybeLightElement(this)?.let { CandidateInfo(it, PsiSubstitutor.EMPTY) }
            }.asIterable()
    } ?: emptyList()
}

//TODO maybe remove it if it is unused?
class KotlinUComponentQualifiedReferenceExpression(
    override val psi: KtDestructuringDeclarationEntry,
    givenParent: UElement?
) : KotlinAbstractUExpression(givenParent), UQualifiedReferenceExpression, UMultiResolvable,
    KotlinUElementWithType, KotlinEvaluatableUElement {
    override val accessType = UastQualifiedExpressionAccessType.SIMPLE

    override lateinit var receiver: UExpression
        internal set

    override lateinit var selector: UExpression
        internal set

    override val resolvedName: String?
        get() = psi.analyze()[BindingContext.COMPONENT_RESOLVED_CALL, psi]?.resultingDescriptor?.name?.asString()

    override fun resolve(): PsiElement? {
        val bindingContext = psi.analyze()
        val descriptor = bindingContext[BindingContext.COMPONENT_RESOLVED_CALL, psi]?.resultingDescriptor ?: return null
        return descriptor.toSource()?.getMaybeLightElement(this)
    }

    override fun multiResolve(): Iterable<ResolveResult> = listOfNotNull(resolve()?.let { CandidateInfo(it, PsiSubstitutor.EMPTY) })
}