/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator.sentencebuilder;

import com.google.gdata.util.common.base.Pair;
import grammar.generator.SubjectType;
import util.exceptions.QueGGMissingFactoryClassException;

/**
 *
 * @author elahi
 */
public class QuestionFinder {

    public static Boolean isIntergativePronoun(String template) throws QueGGMissingFactoryClassException {
        if (template.contains(SubjectType.interrogativePronoun.name())
                || template.contains(SubjectType.interrogativeAmount.name())
                || template.contains(SubjectType.interrogativePronounWhom.name())
                || template.contains(SubjectType.interrogativeOften.name())
                || template.contains(SubjectType.interrogativeMuch.name())
                || template.contains(SubjectType.interrogativeEvalution.name())
                || template.contains(SubjectType.interrogativePlace.name())
                || template.contains(SubjectType.interrogativeDeterminer.name())
                || template.contains(SubjectType.interrogativeVariableDeterminer.name())
                || template.contains(SubjectType.interrogativePronounDeterminer.name())
                || template.contains(SubjectType.interrogativePlace.name())
                || template.contains(SubjectType.interrogativeTemporal.name())) {
            return true;
        }

        return false;
    }

}
