/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.inspections

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.core.replaced
import org.jetbrains.kotlin.idea.inspections.collections.isCalling
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.*

class ReplaceCollectionCountWithSizeInspection : AbstractKotlinInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return simpleNameExpressionVisitor { simpleNameExpression ->
            if (simpleNameExpression.isCount()) {
                holder.registerProblem(
                    simpleNameExpression,
                    "Replace 'count' with 'size'",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                    ReplaceCollectionCountWithSizeQuickFix()
                )
            }
        }
    }
}

class ReplaceCollectionCountWithSizeQuickFix : LocalQuickFix {
    override fun getName() = "Replace 'count' with 'size'"

    override fun getFamilyName() = name

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement as? KtSimpleNameExpression ?: return
        element.replaced(
            KtPsiFactory(element).createExpressionByPattern("size")
            // remove ().
        )
    }
}

private fun KtSimpleNameExpression.isCount(): Boolean {
    val callExpression = (parent as? KtCallExpression) ?: return false
    return transformations.any { callExpression.isCalling(FqName(it)) }
}

private val transformations = listOf(
//    "java.util.ArrayList.count",
//    "java.util.HashMap.isEmpty",
//    "java.util.HashSet.isEmpty",
//    "java.util.LinkedHashMap.isEmpty",
//    "java.util.LinkedHashSet.isEmpty",
    "kotlin.collections.count"//,
//    "kotlin.collections.List.isEmpty",
//    "kotlin.collections.Set.isEmpty",
//    "kotlin.collections.Map.isEmpty",
//    "kotlin.collections.MutableList.isEmpty",
//    "kotlin.collections.MutableSet.isEmpty",
//    "kotlin.collections.MutableMap.isEmpty"
)