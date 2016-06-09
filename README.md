# Mule Example - Stream Gigabyte CSV into Database

## Overview
This example Mule project shows how to stream a large CSV file into a database by splitting the file into smaller chunks. A custom Message Processor is used to split the CSV file by a predefined number of rows. Those chunks are then sent to a VM Queue and distributed to batch flows. Each batch flow inserts data using bulk mode through a batch commit into a MySQL database. Errors with records are caught and written to a file.

## Components
* Custom Message Processor
* VM Queue
* Batch Flow
* Batch Commit
* DataWeave
* Choice Exception

## Resources
* Table SQL Script - https://github.com/djuang1/mule-chunk-csv-into-database/blob/master/src/main/resources/csvmap1.sql
* Example Data - https://github.com/djuang1/mule-chunk-csv-into-database/blob/master/src/main/resources/data-with-error.csv