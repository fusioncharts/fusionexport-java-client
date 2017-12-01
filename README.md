# FusionExport Java Client

Language SDK for FusionExport which enables exporting of charts & dashboards through Java.

## Installation

### Gradle

To use in a Gradle project, first add the maven central repository to your repositories list:

```groovy
repositories {
    mavenCentral()
}
```

Then, just add this SDK as a dependency to your `build.gradle` file:

```groovy
dependencies {
    compile "com.fusioncharts.fusionexport:fusionexport:1.0.0-beta"
}
```

### Maven

To use this SDK with your maven project, add this dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>com.fusioncharts.fusionexport</groupId>
  <artifactId>fusionexport</artifactId>
  <version>1.0.0-beta</version>
</dependency>
```

## Getting Started

After adding the dependency, create a new file named `chart-config.json` containing all 
the chart configurations that are to be exported. Before exporting your chart, make sure
the export server is running.

The `chart-config.json` file looks as shown below:

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

Now, import the SDK library into your project and write the export logic as follows:

```java
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import com.fusioncharts.fusionexport.client.*; // import sdk

public class ExportChart implements ExportDoneListener, ExportStateChangedListener {

    public static void main(String[] args) {

        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", readFile("fullpath/of/chart-config-file.json"));

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Call the export() method with the export config and the respective callbacks
        em.export(config, new ExportChart(), new ExportChart());
    }

    @Override // Called when export is done
    public void exportDone(String result, ExportException error) {
        if (error != null) {
            System.out.println(error.getMessage());
        } else {
            System.out.println("DONE: " + result);
        }
    }

    @Override // Called on each export state change
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
Finally, run your Java app, and the exported chart is received via the `ExportDone` event.

## API Reference

You can find the full reference [here](https://www.fusioncharts.com/dev/exporting-charts/using-fusionexport/sdk-api-reference/java.html)