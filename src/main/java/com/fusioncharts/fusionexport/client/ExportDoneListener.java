package com.fusioncharts.fusionexport.client;

import java.io.IOException;

public interface ExportDoneListener {
    void exportDone(ExportDoneData result, ExportException error) throws IOException;
}
