/*
 * This file is part of sonar-icode-cnes-plugin.
 *
 * sonar-icode-cnes-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sonar-icode-cnes-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with sonar-icode-cnes-plugin.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.cnes.sonar.plugins.icode.rules;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;

import fr.cnes.sonar.plugins.icode.languages.Fortran77Language;
import fr.cnes.sonar.plugins.icode.languages.Fortran90Language;
import fr.cnes.sonar.plugins.icode.settings.ICodePluginProperties;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Specific i-Code rules definition provided by resource file.
 */
public class ICodeRulesDefinition implements RulesDefinition {

	public static final String REPOSITORY = "f77-rules";
  	public static final String FORTRAN_LANGUAGE = "f77";
  	public static final RuleKey F77_DATA_ARRAY = RuleKey.of(REPOSITORY, "F77.DATA.Array");

	/** Partial key for repository. **/
	private static final String REPO_KEY_SUFFIX = "-rules";


	/** Path to xml file in resources tree (fortran 77 rules). **/
	public static final String PATH_TO_F77_RULES_XML = "/rules/icode-f77-rules.xml";

	/** Path to xml file in resources tree (fortran 90 rules). **/
	public static final String PATH_TO_F90_RULES_XML = "/rules/icode-f90-rules.xml";

	/**
	 * Define i-Code rules in SonarQube thanks to xml configuration files.
	 *
	 * @param context SonarQube context.
	 */
	@Override
	public void define(final Context context) {
		// createRepository(context, Fortran77Language.KEY);
		//createRepository(context, Fortran90Language.KEY);
		NewRepository repository = context.createRepository(REPOSITORY, FORTRAN_LANGUAGE).setName(ICodePluginProperties.ICODE_NAME);

		NewRule f77DataArray = repository.createRule(F77_DATA_ARRAY.rule())
			.setName("F77.DATA.Array")
			.setInternalKey("*")
			.setHtmlDescription("Arrays dimension should be declared explicitly. The use of * is tolerated for the last one if justified with a comment.")
			.setSeverity(Severity.MAJOR)
			.setStatus(RuleStatus.READY)
			.setType(RuleType.CODE_SMELL);
		
		f77DataArray.setDebtRemediationFunction(f77DataArray.debtRemediationFunctions().constantPerIssue("30min"));

		repository.done();
	}

	// /**
	//  * Create repositories for each language.
	//  *
	//  * @param context SonarQube context.
	//  * @param language Key of the language.
	//  */
	// protected void createRepository(final Context context, final String language) {
	// 	// Create a repository to put rules inside.
	// 	final NewRepository repository = context
    //             .createRepository(getRepositoryKeyForLanguage(language), language)
    //             .setName(getRepositoryName());

	// 	// Get XML file describing rules for language.
	// 	final InputStream rulesXml = this.getClass().getResourceAsStream(rulesDefinitionFilePath(language));
	// 	// Add rules in repository.
	// 	if (rulesXml != null) {
	// 		final RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
	// 		rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
	// 	}
	// 	repository.done();
	// }

	/**
     * Getter for repository key.
	 *
	 * @param language Key of the related language.
     * @return A string "language-key".
     */
    public static String getRepositoryKeyForLanguage(final String language) {
        return language + REPO_KEY_SUFFIX;
    }

    /**
     * Getter for the path to rules file.
     *
	 * @param language Key of the language.
     * @return A path in String format.
     */
	public String rulesDefinitionFilePath(final String language) {
		String path = "bad_file";
		switch (language) {
			case Fortran77Language.KEY:
				path = PATH_TO_F77_RULES_XML;
				break;
			case Fortran90Language.KEY:
				path = PATH_TO_F90_RULES_XML;
				break;
			default:
				break;
		}
		return path;
	}
}

