/**
 * Copyright (c) 2012-2017 DataTorrent, Inc.
 * All Rights Reserved.
 * The use of this source code is governed by the Limited License located at
 * https://www.datatorrent.com/datatorrent-openview-software-license/
 */

package com.datatorrent.image;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;

@ApplicationAnnotation(name = "Image-Processing-Example-Application")
/**
 * Reads images from a directory- converts,compresses and resize the images and writes to output directory.
 * Set values in properties.xml
 */
public class Application implements StreamingApplication
{

  public void populateDAG(DAG dag, Configuration configuration)
  {
    /**
     * Add dag operators.
     */
    ImageReader imageReader = dag.addOperator("ImageReader",ImageReader.class);
    ImageFormatConversionOperator imageFormatConversionOperator = dag.addOperator("ConvertToJpeg",ImageFormatConversionOperator.class);
    ImageCompressionOperator imageCompressionOperator = dag.addOperator("CompressImage",ImageCompressionOperator.class);
    ImageResizeOperator imageResizeOperator = dag.addOperator("ResizeImage",ImageResizeOperator.class);
    BytesFileWriter bytesFileWriter = dag.addOperator("ImageWriter",BytesFileWriter.class);
    /**
     * Add dag streams.
     */
    dag.addStream("Read to convert",imageReader.output,imageFormatConversionOperator.input);
    dag.addStream("Convert to compress",imageFormatConversionOperator.output,imageCompressionOperator.input);
    dag.addStream("Compress to resize",imageCompressionOperator.output,imageResizeOperator.input);
    dag.addStream("Resize to Write",imageResizeOperator.output,bytesFileWriter.input);
  }
}
