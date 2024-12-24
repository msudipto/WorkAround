/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 * Modified by Tanmay Ghosh
 * Modified by Vivek Bengre
 */
@SpringBootApplication
public class PetClinicApplication extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(PetClinicApplication.class);

    public static void main(String[] args) {
        try {
            logger.info("Starting PetClinic Application");
            SpringApplication.run(PetClinicApplication.class, args);
            logger.info("PetClinic Application Started Successfully");
        } catch (Exception e) {
            logger.error("Error occurred while starting PetClinic Application", e);
            throw e;  // Optionally rethrow to allow Spring to handle the error
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(PetClinicApplication.class);
    }
}