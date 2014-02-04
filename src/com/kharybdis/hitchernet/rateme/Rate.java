package com.kharybdis.hitchernet.rateme;

import android.app.Activity;

public class Rate
{
  public static RateMeMaybe showRatePrompt(Activity paramActivity)
  {
    RateMeMaybe localRateMeMaybe = new RateMeMaybe(paramActivity);
    localRateMeMaybe.setPromptMinimums(5, 2, 5, 2);
    localRateMeMaybe.setRunWithoutPlayStore(Boolean.valueOf(true));
    localRateMeMaybe.setAdditionalListener(new RateMeMaybe.OnRMMUserChoiceListener()
    {
      public void handleNegative() {}
      
      public void handleNeutral() {}
      
      public void handlePositive() {}
    });
    localRateMeMaybe.setDialogMessage("You really seem to like this app, since you have already received/sent %totalLaunchCount%  files! It would be great if you took a moment to rate it.");
    localRateMeMaybe.setDialogTitle("Rate this app");
    localRateMeMaybe.setPositiveBtn("Yeeha!");
    localRateMeMaybe.run();
    return localRateMeMaybe;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.rateme.Rate
 * JD-Core Version:    0.7.0.1
 */