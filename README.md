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
    compile "com.fusioncharts.fusionexport:fusionexport:1.0.0"
}
```

### Maven

To use this SDK with your maven project, add this dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>com.fusioncharts.fusionexport</groupId>
  <artifactId>fusionexport</artifactId>
  <version>1.0.0</version>
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
import com.fusioncharts.fusionexport.client.*; // import sdk

public class ExportChart {
    public static void main(String[] args) throws Exception {

        String configPath = "fullpath/of/chart-config.json";

        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", configPath);

        // Instantiate the ExportManager class
        ExportManager manager = new ExportManager(config);
        // Call the export() method with the export config and the respective callbacks
        manager.export(new ExportDoneListener() {
                           @Override
                           public void exportDone(ExportDoneData result, ExportException error) {
                               if (error != null) {
                                   System.out.println(error.getMessage());
                               } else {
                                   ExportManager.saveExportedFiles("fullPath", result);
                               }
                           }
                       },
                new ExportStateChangedListener() {
                    @Override
                    public void exportStateChanged(ExportState state) {
                        System.out.println("STATE: " + state.reporter);
                    }
                });
    }
}
```
Finally, run your Java app, and the exported chart is received via the `ExportDone` event.

## API Reference

You can find the full reference [here](https://www.fusioncharts.com/dev/exporting-charts/using-fusionexport/sdk-api-reference/java.html)