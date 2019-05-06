package com.antoine.services;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * <b>Classe de service container et d'injection de dépendance.</b>
 * <p>gère une mémoire cache pour reconstituer les beans</p>
 *
 * @author antoine
 */
public class Assembler {

	/**association entre l'id du bean et sa classe*/
	private Map<String, String> id_class;

	/**association entre id bean et l'id de ses paramètres sous forme d'array*/
	private Map<String, String[]> idBean_idParameters;

	/**id_paramètre et leur valeur*/
	private Map<String, String> idParam_value;

	/**id_paramètre et leur méthode d'injection*/
	private Map<String, String> idParam_methods;

	/**
	 * Construit l'assembleur selon
	 * @param filePath le path du fichier de configuration
	 */
	public Assembler(String filePath) {
		idBean_idParameters= new HashMap<>();
		id_class= new HashMap<>();
		idParam_value= new HashMap<>();
		idParam_methods= new HashMap<>();
		parse(filePath);
	}


	/**
	 * <p>Parse le fichier XML.</p>
	 */
	private void parse(String filePath) {
		SAXParserFactory factory= SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();

			parser.parse(filePath, new XMLHandler());
		} catch (SAXException  | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("erreur de lecture du fichier de configuration");
		}
	}


	/**
	 *
	 * @param id correspondant aux Ids du fichier de configuration des beans.
	 * @return une instance
	 */
	public Object newInstance(String id) {
		if(!id_class.containsKey(id)){
			throw new RuntimeException("id non présente dans le fichier de configuration");
		}
		return getBean(id);
	}


	/**
	 *
	 * @param id corrspondant aux Ids du fichier de configuration des beans
	 * @return une instance
	 */
	private Object getBean(String id) {

		Object bean= null;
		try {
			Method m;
			Class<?> beanClass = Class.forName(id_class.get(id));
			Constructor<?> beanConstructor = beanClass.getConstructor();
			bean = beanConstructor.newInstance();
			if(idBean_idParameters.containsKey(id)) {

				String[] parameters = idBean_idParameters.get(id);

				for (int i = 0; i < parameters.length; i++) {

					if (id_class.containsKey(parameters[i])) {
						Object o = getBean(parameters[i]);
						if (o.getClass().getInterfaces().length != 0) {

							if(o.getClass().getInterfaces().length > 1){
								m= findMethod(bean.getClass(), o.getClass().getInterfaces(), parameters[i]);
							}else
								m = beanClass.getMethod(idParam_methods.get(parameters[i]), o.getClass().getInterfaces());
						} else
							m = beanClass.getMethod(idParam_methods.get(parameters[i]), o.getClass());
						m.invoke(bean, o);

					} else {

						String param = idParam_value.get(parameters[i]);
						m = beanClass.getMethod(idParam_methods.get(parameters[i]), parameters[i].getClass());
						m.invoke(bean, param);
					}

				}
			}

		}catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
		IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("erreur lors de la lecture du fichier de configuration du bean: "+id);
		}

		return bean;
	
	}

	/**
	 * <p>Trouve la méthode d'une classe en fonction de son nom et des types possible de paramètre</p>
	 * @param aClass dont la méthode est à trouver
	 * @param interfaces les types du paramètre
	 * @param idParam correspondant au fichier de config
	 * @return la méthode correspondant au type de son objet en paramètre, null sinon
	 */
	private Method findMethod(Class<?> aClass, Class<?>[] interfaces, String idParam) {
		String methodName= idParam_methods.get(idParam);
		Method[] methods= aClass.getMethods();

		for(int i= 0; i < methods.length; i++){
			if(methodName.equals(methods[i].getName())){
				Class<?>[] paramterTypes= methods[i].getParameterTypes();

				for(Class<?> c:paramterTypes){
					for(Class<?> c2:interfaces){
						if(c.getName().equals(c2.getName())){
							return methods[i];
						}
					}

				}

			}

		}
		return null;
	}

	/**
	 * <b>Classe interne pour gérer la lecture du fichier .xml</b>
	 * <p>lecture en mode event et gestion de l'enregistrement d'un bean en mémoire cache</p>
	 * @author antoine
	 */
	class XMLHandler extends DefaultHandler {

		/**id du bean courrant, au fil de la lecture du document*/
		private Stack<String> currentBean= new Stack<>();

		/**utilisé pour ranger le paramètre au prochain index libre
		dans l'array de la Map idBean_parameters*/
		private Stack<Integer> indexCurrentBean= new Stack<>();

		/**
		 *<p>au démarrage de la lecture d'un tag</p>
		 * @param nameSpaceURI non utilisé
		 * @param lName non utilisé
		 * @param qName nom du tag
		 * @param attr l'array de ses attributs
		 */
		@Override
		public void startElement(String nameSpaceURI, String lName, String qName,
				Attributes attr) {

			int length= 0;

			if(qName.equals("bean")) {

				registerBean(attr);

			}else if(qName.equals("injection")){

				registerInjection(attr);

			}else if(qName.equals("parameters")){

				length= Integer.parseInt(attr.getValue("nbr"));
				idBean_idParameters.put(currentBean.peek(), new String[length]);

			}else if(qName.equals("paramInjection") || qName.equals("parameter")) {

				registerParameter(attr);
			}

		}


		/**
		 *<p>enregistre en mémoire cache le nom de l'injection pour le bean courant</p>
		 * @param attr l'array des attributs du tag
		 */
		private void registerInjection(Attributes attr) {
			int index= indexCurrentBean.pop();

			idBean_idParameters.get(currentBean.peek())[index]= attr.getValue("id");

			indexCurrentBean.push(++index);

			registerBean(attr);

		}


		/**
		 *<p>enregistre en mémoire cache le paramètre à injecter au bean courant</p>
		 * @param attr l'array des attributs du tag
		 */
		private void registerParameter(Attributes attr) {

			String idParam= "";

			for(int i= 0; i< attr.getLength(); i++) {

				if(attr.getLocalName(i).equals("id")) {

					int index= indexCurrentBean.pop();
					idParam= attr.getValue(i);
					idBean_idParameters.get(currentBean.peek())[index]= idParam;
					indexCurrentBean.push(++index);

				}else if(attr.getLocalName(i).equals("value")) {

					idParam_value.put(idParam, attr.getValue(i));

				}else if (attr.getLocalName(i).equals("method")) {

					idParam_methods.put(idParam, attr.getValue(i));
				}
			}
		}

		/**
		 *<p>déclenché à la lecture de balise fermante</p>
		 * @param uri non utilisé
		 * @param localName non utilisé
		 * @param qName nom du tag
		 */
		@Override
		public void endElement(String uri, String localName, String qName) {

			if(qName.equals("bean") || qName.equals("injection")){
				currentBean.pop();
				indexCurrentBean.pop();
			}
		}


		/**
		 *<p>enregistre en mémoire cache le nom du bean courant et sa classe</p>
		 * @param attr l'array des attributs du tag
		 */
		private void registerBean(Attributes attr) {

			String id= null, className= null;
			indexCurrentBean.push(0);

			id = attr.getValue("id");
			currentBean.push(id);

			className = attr.getValue("class");

			if (attr.getValue("method") != null)
				idParam_methods.put(id, attr.getValue("method"));

			id_class.put(id, className);
		}
	
	}

}
				