/*
 * Copyright 2002-2013 the original author or authors.
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
package org.springframework.samples.petclinic.owner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/owners")
class OwnerController {

    private final OwnerRepository ownerRepository;
    private final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    public OwnerController(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<String> saveOwner(@RequestBody Owners owner) {
        try {
            ownerRepository.save(owner);
            logger.info("New Owner {} saved", owner.getFirstName());
            return ResponseEntity.status(HttpStatus.CREATED)

                    .body("New Owner " + owner.getFirstName() + " Saved");
        } catch (Exception e) {
            logger.error("Error saving owner", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving owner");
        }
    }

    @GetMapping("/dummy")
    public ResponseEntity<List<Owners>> createDummyData() {
        List<Owners> dummyOwners = List.of(
                new Owners(1, "John", "Doe", "404 Not found", "12345"),
                new Owners(2, "Jane", "Doe", "Secret", "67890")
        );
        return ResponseEntity.ok(dummyOwners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Owners> getOwner(@PathVariable int id) {
        Optional<Owners> owner = ownerRepository.findById(id);
        return owner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }
}