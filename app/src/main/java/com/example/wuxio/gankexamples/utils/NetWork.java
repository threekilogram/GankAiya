package com.example.wuxio.gankexamples.utils;

import com.example.wuxio.gankexamples.App;
import tech.threekilogram.network.state.manager.NetStateChangeManager;
import tech.threekilogram.network.state.manager.NetStateUtils;
import tech.threekilogram.network.state.manager.NetStateValue;

/**
 * @author Liujin 2018-10-07:10:35
 */
public class NetWork {

      public static boolean hasNetwork ( ) {

            int netState = NetStateChangeManager.getCurrentNetState();

            if( netState > NetStateValue.WIFI_MOBILE_DISCONNECT ) {
                  return true;
            }

            return ( NetStateUtils.isWifiConnected( App.INSTANCE )
                || NetStateUtils.isMobileConnected( App.INSTANCE ) );
      }
}
