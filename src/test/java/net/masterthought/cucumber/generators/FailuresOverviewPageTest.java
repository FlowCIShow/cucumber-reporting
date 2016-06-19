package net.masterthought.cucumber.generators;

import mockit.Deencapsulation;
import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import org.apache.velocity.VelocityContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class FailuresOverviewPageTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getWebPage_ReturnsFeatureFileName() {

        // given
        page = new FailuresOverviewPage(reportResult, configuration);

        // when
        String fileName = page.getWebPage();

        // then
        assertThat(fileName).isEqualTo("failures-overview.html");
    }

    @Test
    public void prepareReportAddsCustomProperties() {

        // given
        page = new FailuresOverviewPage(reportResult, configuration);
        // this page only has failed scenarios (elements) so extract them into
        // a list to compare
        List<Element> failures = new ArrayList<>();
        for (Feature feature : features) {
            if (feature.getStatus().isPassed())
                continue;

            for (Element element : feature.getElements()) {
                if (element.getStepsStatus().isPassed())
                    continue;

                failures.add(element);
            }
        }

        // when
        page.prepareReport();

        // then
        VelocityContext context = Deencapsulation.getField(page, "context");
        assertThat(context.getKeys()).hasSize(7);
        assertThat(context.get("failures")).isEqualTo(failures);
    }
}
