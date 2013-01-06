SoundsOfSubconsciousSeas : Object {

  var <>masterChannel,
    <>droneChannel,
    <>waterChannel,
    <>channels,
    <>instrs,
    <>bufs;

  init {

    this.masterChannel = MixerChannel.new(\masterChannel, Server.default, 2, 2, 1.0);
    this.droneChannel = MixerChannel.new(
      \droneChannel,
      Server.default,
      2,
      2,
      0,
      outbus: this.masterChannel
    );
    this.droneChannel.guiUpdateTime = 0.05;
    
    this.waterChannel = MixerChannel.new(
      \waterChannel,
      Server.default,
      2,
      2,
      0,
      outbus: this.masterChannel
    );
    this.waterChannel.guiUpdateTime = 0.05;
    
    this.bufs = (
      splashingWaterBuf: 0
    );

    this.instrs = (
      drone: 0,
      water: 0
    );
  }

  buf_loaded {
    arg bufKey, buf;

    this.bufs[bufKey] = buf;

    this.bufs.do {
      arg item;

      if (item == 0, {
        ^false;    
      });

      this.bufs_all_loaded();

    };

    ^true;
  }

  bufs_all_loaded {

    this.setup_drone();
    this.setup_water();

    {
      2.0.wait();
      this.start_drone();
      this.start_water();

      this.start_soundscape();
    }.fork();

  }

  setup_water {
    arg cb;

    this.instrs[\water] = Patch("cs.sfx.LoopBuf", (
      buf: this.bufs[\splashingWaterBuf],
      gate: KrNumberEditor.new(1, \gate.asSpec())
    ));
  }

  start_water {
    this.waterChannel.play(this.instrs[\water]);
    
  }

  setup_drone {
    this.instrs[\drone] = Patch("SeasOfSubconsciousDrone", (
      freq: 110
    ));

  }

  start_drone {
    this.droneChannel.play(this.instrs[\drone]);
    
  
  }

  start_soundscape {
    var waterLevelLow = 0.1,
      waterLevelHigh = 0.75,
      droneLevelLow = 0.1,
      droneLevelHigh = 0.75,
      droneWaterWaitTime,
      droneWaterWaitTimeMin = 20.0,
      droneWaterWaitTimeMax = 45.0,
      droneWaterTransitionTime = 10.0,
      transitionStaggerTime = 0.5 * droneWaterTransitionTime;

    {

      while ({ true }, {

        // turn water on high
        this.waterChannel.levelTo(waterLevelHigh, droneWaterTransitionTime);
        // stagger
        transitionStaggerTime.wait();
        // turn drone on low
        this.droneChannel.levelTo(droneLevelLow, droneWaterTransitionTime);
        
        // wait
        droneWaterWaitTime = rrand(droneWaterWaitTimeMin, droneWaterWaitTimeMax);
        droneWaterWaitTime.wait();

        // turn drone on high
        this.droneChannel.levelTo(droneLevelHigh, droneWaterTransitionTime);
        // stagger
        transitionStaggerTime.wait();
        // turn water on low
        this.waterChannel.levelTo(waterLevelLow, droneWaterTransitionTime);

        // wait
        droneWaterWaitTime = rrand(droneWaterWaitTimeMin, droneWaterWaitTimeMax);
        droneWaterWaitTime.wait();
      
      });

    }.fork();
  }
}
