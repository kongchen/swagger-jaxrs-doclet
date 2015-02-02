package com.carma.swagger.doclet.translator;

import static com.carma.swagger.doclet.translator.Translator.OptionalName.presentOrMissing;

import com.carma.swagger.doclet.DocletOptions;
import com.carma.swagger.doclet.parser.ParserHelper;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Type;

/**
 * The NameBasedTranslator represents a translator that looks up names of items based on their
 * javadoc signature
 * @version $Id$
 */
public class NameBasedTranslator implements Translator {

	private DocletOptions options;

	/**
	 * This creates a NameBasedTranslator
	 * @param options
	 */
	public NameBasedTranslator(DocletOptions options) {
		super();
		this.options = options;
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#parameterTypeName(boolean, com.sun.javadoc.Parameter, com.sun.javadoc.Type)
	 */
	public OptionalName parameterTypeName(boolean multipart, Parameter parameter, Type paramType) {
		if (paramType == null) {
			paramType = parameter.type();
		}
		// look for File data types
		if (multipart) {
			boolean isFileDataType = ParserHelper.isFileParameterDataType(parameter, this.options);
			if (isFileDataType) {
				OptionalName res = presentOrMissing("File");
				return res;
			}
		}

		return typeName(paramType);
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#typeName(com.sun.javadoc.Type, com.sun.javadoc.ClassDoc[])
	 */
	public OptionalName typeName(Type type, ClassDoc[] views) {
		String[] typeFormat = ParserHelper.typeOf(type, this.options);

		if (views != null && views.length > 0 && !ParserHelper.isPrimitive(type, this.options)) {
			StringBuilder nameWithView = new StringBuilder(typeFormat[0]).append("-");
			for (ClassDoc view : views) {
				nameWithView.append(view.name());
			}
			return presentOrMissing(nameWithView.toString(), typeFormat[1]);
		}

		return presentOrMissing(typeFormat[0], typeFormat[1]);
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#typeName(com.sun.javadoc.Type)
	 */
	public OptionalName typeName(Type type) {
		String[] typeFormat = ParserHelper.typeOf(type, this.options);
		return presentOrMissing(typeFormat[0], typeFormat[1]);
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#fieldName(com.sun.javadoc.FieldDoc)
	 */
	public OptionalName fieldName(FieldDoc field) {
		return presentOrMissing(field.name());
	}

	/**
	 * {@inheritDoc}
	 * @see com.carma.swagger.doclet.translator.Translator#methodName(com.sun.javadoc.MethodDoc)
	 */
	public OptionalName methodName(MethodDoc method) {
		String name = null;
		if ((method.name().startsWith("get") || method.name().startsWith("set")) && method.name().length() > 3) {
			name = method.name().substring(3);
			name = name.substring(0, 1).toLowerCase() + (name.length() > 1 ? name.substring(1) : "");
		} else if (method.name().startsWith("is") && method.name().length() > 2) {
			// verify return type is boolean
			String[] typeFormat = ParserHelper.typeOf(method.returnType(), this.options);
			if ("boolean".equals(typeFormat[0])) {
				name = method.name().substring(2);
				name = name.substring(0, 1).toLowerCase() + (name.length() > 1 ? name.substring(1) : "");
			}
		}
		return presentOrMissing(name);
	}

}
