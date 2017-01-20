package io.chino.examples.schemas;

import io.chino.api.common.indexed;

import java.io.File;
import java.util.Date;

public class SchemaStructureSample {
    //Use this annotation if you want the targeted field indexed in the server
    @indexed
    public int test_integer;
    @indexed
    public String test_string;
    public boolean test_boolean;
    public Date test_date;
    public File test_file;
}
