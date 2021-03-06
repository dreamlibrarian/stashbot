//   Copyright 2013 Palantir Technologies
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
package com.palantir.stash.stashbot.webpanel;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;

import com.atlassian.plugin.web.model.WebPanel;
import com.atlassian.stash.content.Changeset;
import com.atlassian.stash.repository.Repository;
import com.palantir.stash.stashbot.config.ConfigurationPersistenceManager;
import com.palantir.stash.stashbot.config.RepositoryConfiguration;
import com.palantir.stash.stashbot.logger.StashbotLoggerFactory;
import com.palantir.stash.stashbot.managers.JenkinsBuildTypes;
import com.palantir.stash.stashbot.urlbuilder.TriggerBuildUrlBuilder;

public class RetriggerLinkWebPanel implements WebPanel {

    private final ConfigurationPersistenceManager cpm;
    private final TriggerBuildUrlBuilder ub;
    private final Logger log;

    public RetriggerLinkWebPanel(ConfigurationPersistenceManager cpm, TriggerBuildUrlBuilder ub,
        StashbotLoggerFactory lf) {
        this.cpm = cpm;
        this.ub = ub;
        this.log = lf.getLoggerForThis(this);
    }

    @Override
    public String getHtml(Map<String, Object> context) {
        Writer holdSomeText = new StringWriter();
        try {
            writeHtml(holdSomeText, context);
        } catch (IOException e) {
            log.error("Error occured rendering web panel", e);
            return "Error occured loading text";
        }
        return holdSomeText.toString();
    }

    @Override
    public void writeHtml(Writer writer, Map<String, Object> context) throws IOException {
        try {
            Repository repo = (Repository) context.get("repository");
            RepositoryConfiguration rc = cpm.getRepositoryConfigurationForRepository(repo);

            if (!rc.getCiEnabled()) {
                // No link
                return;
            }

            Changeset changeset = (Changeset) context.get("changeset");
            String url = ub.getJenkinsTriggerUrl(repo, JenkinsBuildTypes.VERIFICATION, changeset.getId(), null, null);

            writer.append("<a href=\"" + url + "\">Retrigger</a>");
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }
}
