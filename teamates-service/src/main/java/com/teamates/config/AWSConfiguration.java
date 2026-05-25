package com.teamates.config;

import org.springframework.context.annotation.Configuration;

// FLAW: AWSConfiguration is empty – no actual AWS beans defined
// FLAW: referenced by EventPublisher interface but no implementation wired up
// FLAW: credentials would need to be configured separately but no validation or defaults
@Configuration
public class AWSConfiguration {
    // TODO: configure SQS/SNS clients when messaging is implemented
    // FLAW: region, queue URL etc. should be injected from application.yaml but are not
}
