package com.eightycats.litterbox.logging;

public interface ILogger
{
   public void log( String message );

   public void debug( String message );

   public void warning( String message );

   public void error( String message );

   public void logStackTrace(Throwable oops);
}
