package org.tandrup.drdownloader

import groovy.json.JsonSlurper

def json = new JsonSlurper().parseText(new URL("http://www.dr.dk/NU/api/programseries").text);

json.each { item -> println item.title + "\t" + item.slug }
