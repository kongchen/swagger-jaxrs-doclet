package com.carma.swagger.doclet.translator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Function;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

/**
 * The FirstNotNullTranslator represents a chain of translators.
 * It will iterate through each one in the chain and return the results of the first translator
 * that does not return null.
 * @version $Id$
 */
public class FirstNotNullTranslator implements Translator {

	private final List<Translator> chain;

	/**
	 * This creates a FirstNotNullTranslator with no links
	 */
	public FirstNotNullTranslator() {
		this.chain = new ArrayList<Translator>();
	}

	/**
	 * This adds another translator link to the chain of translators
	 * @param link The translator to add
	 * @return this
	 */
	public FirstNotNullTranslator addNext(Translator link) {
		this.chain.add(link);
		return this;
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#typeName(com.sun.javadoc.Type, com.sun.javadoc.ClassDoc[])
	 */
	public OptionalName typeName(final Type type, final ClassDoc[] views) {
		return firstNotNullOf(new Function<Translator, OptionalName>() {

			public OptionalName apply(Translator translator) {
				return translator.typeName(type, views);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#parameterTypeName(boolean, com.sun.javadoc.Parameter, com.sun.javadoc.Type)
	 */
	public OptionalName parameterTypeName(final boolean multipart, final Parameter parameter, final Type paramType) {
		return firstNotNullOf(new Function<Translator, OptionalName>() {

			public OptionalName apply(Translator translator) {
				return translator.parameterTypeName(multipart, parameter, paramType);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#typeName(com.sun.javadoc.Type)
	 */
	public OptionalName typeName(final Type type) {
		return firstNotNullOf(new Function<Translator, OptionalName>() {

			public OptionalName apply(Translator translator) {
				return translator.typeName(type);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#fieldName(com.sun.javadoc.FieldDoc)
	 */
	public OptionalName fieldName(final FieldDoc field) {
		return firstNotNullOf(new Function<Translator, OptionalName>() {

			public OptionalName apply(Translator translator) {
				return translator.fieldName(field);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#methodName(com.sun.javadoc.MethodDoc)
	 */
	public OptionalName methodName(final MethodDoc method) {
		return firstNotNullOf(new Function<Translator, OptionalName>() {

			public OptionalName apply(Translator translator) {
				return translator.methodName(method);
			}
		});
	}

	private OptionalName firstNotNullOf(Function<Translator, OptionalName> function) {
		OptionalName name = null;
		Iterator<Translator> iterator = this.chain.iterator();
		while ((name == null || name.isMissing()) && iterator.hasNext()) {
			name = function.apply(iterator.next());
		}
		return name;
	}

}
