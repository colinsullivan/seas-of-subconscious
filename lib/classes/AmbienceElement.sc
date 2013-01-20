AmbienceElement : SoundscapeElement {
  var <>droneChannel,
    <>waterChannel,
    <>waterPatch,
    <>dronePatch;

  init {
    arg args;

    super.init(args);

    this.waterChannel = MixerChannel.new(
      this.key,
      Server.default,
      2,
      2,
      1.0,
      outbus: this.outChannel
    );
    this.waterChannel.guiUpdateTime = 0.05;

    this.droneChannel = MixerChannel.new(
      this.key,
      Server.default,
      2,
      2,
      1.0,
      outbus: this.outChannel
    );
    this.droneChannel.guiUpdateTime = 0.05;
    
    // drone is mostly going to reverb
    this.droneChannel.newPreSend(
      this.soundscape.reverbReturn,
      -12.0.dbamp()
    );
    
  }

  prepare_to_play {

    super.prepare_to_play();
    
    this.waterPatch = Patch("cs.sfx.LoopBuf", (
      buf: this.soundscape.bufs[\splashingWaterBuf],
      gate: KrNumberEditor.new(1, \gate.asSpec())
    ));
    this.dronePatch = Patch("SeasOfSubconsciousDrone", (
      freq: 110
    ));
  
  }

  play {
    var waterLevelLow = -25.0.dbamp(),
      waterLevelHigh = -14.0.dbamp(),
      droneLevelLow = -25.0.dbamp(),
      droneLevelHigh = -10.0.dbamp(),
      waitTime,
      waitTimeMin = 20.0,
      waitTimeMax = 45.0,
      droneWaterTransitionTime = 10.0,
      transitionStaggerTime = 0.5 * droneWaterTransitionTime;

    {

      this.waterChannel.play(this.waterPatch);
      this.droneChannel.play(this.dronePatch);

      while ({ true }, {

        // turn water on high
        this.waterChannel.levelTo(waterLevelHigh, droneWaterTransitionTime);
        // stagger
        transitionStaggerTime.wait();
        // turn drone on low
        this.droneChannel.levelTo(droneLevelLow, droneWaterTransitionTime);
        
        // wait
        waitTime = rrand(waitTimeMin, waitTimeMax);
        waitTime.wait();

        // turn drone on high
        this.droneChannel.levelTo(droneLevelHigh, droneWaterTransitionTime);
        // stagger
        transitionStaggerTime.wait();
        // turn water on low
        this.waterChannel.levelTo(waterLevelLow, droneWaterTransitionTime);

        // wait
        waitTime = rrand(waitTimeMin, waitTimeMax);
        waitTime.wait();
      
      });

    }.fork();

  
  }

}
