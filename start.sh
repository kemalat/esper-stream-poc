#!/bin/bash
java -Dspring.profiles.active=local -Xms512m -Xmx512m -cp oriontec-esper-1.0.0.jar:lib/* de.oriontec.esper.app.Main
