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
    compile "com.fusioncharts.fusionexport:fusionexport:1.2.0"
}
```

### Maven

To use this SDK with your maven project, add this dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>com.fusioncharts.fusionexport</groupId>
  <artifactId>fusionexport</artifactId>
  <version>2.0.0</version>
</dependency>
```

## Getting Started

### Prerequisite
Ensure that you have FusionExport Service up and running, import the SDK library into your project and write the export logic as follows.

Start with a simple chart export. For exporting a single chart just pass the chart configuration as you would have passed it to the FusionCharts constructor.


```java
import com.fusioncharts.fusionexport.client.*;

public class ExportChart {
    public static void main(String[] args) throws Exception {

        StringBuilder chartConf = new StringBuilder();
        chartConf.append("[");
        chartConf.append("  {");
        chartConf.append("    \"type\": \"column2d\",");
        chartConf.append("    \"renderAt\": \"chart-container\",");
        chartConf.append("    \"width\": \"600\",");
        chartConf.append("    \"height\": \"200\",");
        chartConf.append("    \"dataFormat\": \"json\",");
        chartConf.append("    \"dataSource\": {");
        chartConf.append("      \"chart\": {");
        chartConf.append("        \"caption\": \"Number of visitors last week\",");
        chartConf.append("        \"subCaption\": \"Bakersfield Central vs Los Angeles Topanga\"");
        chartConf.append("      },");
        chartConf.append("      \"data\": [");
        chartConf.append("        {");
        chartConf.append("          \"label\": \"Mon\",");
        chartConf.append("          \"value\": \"15123\"");
        chartConf.append("        },");
        chartConf.append("        {");
        chartConf.append("          \"label\": \"Tue\",");
        chartConf.append("          \"value\": \"14233\"");
        chartConf.append("        },");
        chartConf.append("        {");
        chartConf.append("          \"label\": \"Wed\",");
        chartConf.append("          \"value\": \"25507\"");
        chartConf.append("        }");
        chartConf.append("      ]");
        chartConf.append("    }");
        chartConf.append("  }");
        chartConf.append("]");

        // Instantiate the ExportManager class
        ExportManager em = new ExportManager();
        // Instantiate the ExportConfig class and add the required configurations
        ExportConfig config = new ExportConfig();
        config.set("chartConfig", chartConf.toString());
        config.set("type", "png");

        String[] files = em.export(config, ".", true);

        for(String f : files) {
        	System.out.println(f);
        }
    }
}
```

## Example Usage
In the repository you can find a example folder which contains a considerable number of examples and its resources.
To test just copy the example code into `ExportChart.java` and give the correct path for the resources needed.

## API Reference

You can find the full reference [here](https://www.fusioncharts.com/dev/exporting-charts/using-fusionexport/sdk-api-reference/java.html)
