package com.mulesoft.services.file;
 
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.mule.DefaultMuleMessage;
import org.mule.api.DefaultMuleException;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.client.LocalMuleClient;
import org.mule.api.processor.MessageProcessor;
import org.mule.config.i18n.MessageFactory;
import org.mule.util.IOUtils;


/**
 * modification to paul.anderson's original code. instead of returning an ArrayList<String>, this returns
 * a string to allow DataWeave to interpret the bytes correctly in order to parse the CSV data. 
 * returning the data as an ArrayList<String> removed the new line characters that are needed for
 * DataWeave to parse the data correctly.
 * 
 * generates batches of rows from a file - splits the file at carriage-returns and accumulates 
 * rows in fixed size groupings (see flowVar "lineGroupSize". Each batch of lines is dispatched 
 * asynchronously to VM-queue asyncQueue for processing. Once the file-stream is exhausted, checks
 * to see if any lines remain to be sent
 * 
 * Implements MessageProcessor so that it fits in the Mule flow
 * @author dejim.juang
 * @author paul.anderson
 *
 */
public class FileRowGenerator implements MessageProcessor {
	
  @Override
  public MuleEvent process(MuleEvent event)
      throws MuleException {
    MuleMessage msg = event.getMessage();
    String runID = msg.getInvocationProperty("runID", "" + System.currentTimeMillis());
    int fileNum = msg.getInvocationProperty("fileNum", 0);
    InputStream fis = (InputStream) event.getMessage().getPayload();
   
    String originalFileName = msg.getInboundProperty("originalFileName");
    InputStreamReader isr = new InputStreamReader(fis);
    LineNumberReader lnr = new LineNumberReader(isr);
    String line; 
    String lines = "";
    MuleContext muleContext = event.getMuleContext();
    LocalMuleClient client = muleContext.getClient();
    
    try {
      int incr  = Integer.parseInt(msg.getInvocationProperty("lineGroupSize").toString());
      int lineCount = 0; 
       
      //skip first line
      lnr.readLine();
      
      while ((line = lnr.readLine()) != null) {
        lineCount++;        
        lines += line + "\n";
        
        if (lineCount % incr == 0){          	
        	DefaultMuleMessage localMsg = new DefaultMuleMessage(lines, muleContext);        	
        	localMsg.setOutboundProperty("runID", runID);
        	localMsg.setOutboundProperty("fileNumber", fileNum++);
        	muleContext.getClient().dispatch("vm://asyncQueue", localMsg);        	
        	lines = "";
        }
      }
      
      if (! lines.isEmpty()){
        DefaultMuleMessage localMsg = new DefaultMuleMessage(lines, muleContext);
        localMsg.setOutboundProperty("runID", runID);
        localMsg.setOutboundProperty("fileNumber", fileNum++);
        client.send("vm://asyncQueue", localMsg);
      }
    } catch (IOException e) {
      //best-practice: throw MuleException so Mule's exception-handling and tracking handles it seamlessly
      //best-practice: include essential tracking information
      String errorMessage = String.format("error processing file %s msgID: %S", originalFileName, msg.getCorrelationId());
      throw new DefaultMuleException(MessageFactory.createStaticMessage(errorMessage, e));
    } finally {
      //best practice: close the input stream safely as soon as we know we are done with it 
      IOUtils.closeQuietly(fis);
    }
    
    //best practice: clearly state the status of processing. Typically this would be JSON with status, codes etc.
    // but that depends on the specific use-case
    String finalMessage = String.format("file %s has been split into batches which have been dispatched asynchronously for processing", originalFileName);
    //best practice: modify the message and update with useful status.
    event.setMessage(new DefaultMuleMessage(finalMessage, muleContext));
    return event;
  }
}
