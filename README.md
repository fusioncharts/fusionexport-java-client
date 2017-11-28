# FusionExport Java Client SDK

FusionExport client SDK for Java applications to export charts provided by FusionCharts Javascript library.

## Minimum Required JDK

FusionExport Java SDK requires Java 1.6 and higher.

## Installation

### Maven

To use this SDK in your maven project add this dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>com.fusioncharts.fusionexport.client</groupId>
  <artifactId>fusionexport-java-client</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Gradle

To use in Gradle project add the maven central repository to your repositories list:

```groovy
repositories {
    mavenCentral()
}
```

Then, just add this SDK as dependency to your `build.gradle` file:

```groovy
dependencies {
    compile "com.fusioncharts.fusionexport.client:fusionexport-java-client:1.0.0"
}
```

## Getting Started

After adding the dependency, create a new file named `chart-config.json` which contains
the chart configurations to be exported. Before exporting your chart, make sure
the export server is running.

The `chart-config.json` file:

```json
[
  {
    "type": "column2d",
    "renderAt": "chart-container",
    "width": "600",
    "height": "200",
    "dataFormat": "json",
    "dataSource": {
      "chart": {
        "caption": "Number of visitors last week",
        "subCaption": "Bakersfield Central vs Los Angeles Topanga"
      },
      "data": [
        {
          "label": "Mon",
          "value": "15123"
        },
        {
          "label": "Tue",
          "value": "14233"
        },
        {
          "label": "Wed",
          "value": "25507"
        }
      ]
    }
  }
]
```

Now import the SDK library into your project and write export logic as follows:

```java
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import com.fusioncharts.fusionexport.client.*;

public class ExportChart implements ExportDoneListener, ExportStateChangedListener {

    public static void main(String[] args) {
        ExportChart ec = new ExportChart();

        String chartConfigFile = "fullpath/of/chart-config.json";
        String exportServerIP = "127.0.0.1"; // The IP address of export server
        String exportServerPort = 1337; // The Port of export server

        // The export configurations used by export server
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", readFile(chartConfigFile));

        ExportManager em = new ExportManager(exportServerIP, exportServerPort);
        Exporter exporter = em.export(config, ec, ec);
    }

    @Override
    public void exportDone(String result, ExportException error) {
        if (error != null) {
            System.out.println(error.getMessage());
        } else {
            System.out.println("DONE: " + result);
        }
    }

    @Override
    public void exportStateChanged(String state) {
        System.out.println("STATE: " + state);
    }

    private static String readFile(String file) {
        String fileContent = "";
        try {
            File f = new File(file);
            FileInputStream inp = new FileInputStream(f);
            byte[] bf = new byte[(int) f.length()];
            inp.read(bf);
            fileContent = new String(bf, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
```

Now run your Java app, the exported chart will be received on `ExportDone` event.
