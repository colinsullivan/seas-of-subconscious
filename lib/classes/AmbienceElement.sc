/**
 *  @file       AmbienceElement.sc
 *
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Manages the drone and ambient water sounds.
 **/
AmbienceElement : SoundscapeElement {
  var <>droneChannel,
    <>waterChannel,
    <>waterPatch,
    <>dronePatch,
    <>droneFilterPatch;

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

    this.droneFilterPatch = FxPatch("cs.fx.LPFerModulated", (
      numChan: 2,
      cutoffMinFreq: 100,
      cutoffMaxFreq: 135,
      cutoffModFreq: 0.05
    ));
    
    // drone is mostly going to reverb
    this.droneChannel.newPreSend(
      this.soundscape.reverbReturn,
      -13.0.dbamp()
    );

    this.outChannel.level = -3.0.dbamp();
    
  }

  prepare_to_play {

    super.prepare_to_play();
    
    this.waterPatch = Patch("cs.sfx.LoopBuf", (
      buf: this.bufManager.bufs[\splashingWaterBuf],
      gate: KrNumberEditor.new(1, \gate.asSpec())
    ));
    this.dronePatch = Patch("SeasOfSubconsciousDrone", (
      freq: 110
    ));
  }

  play {
    var waterLevelLow = -27.0.dbamp(),
      waterLevelHigh = -17.0.dbamp(),
      droneLevelLow = -25.0.dbamp(),
      droneLevelHigh = -10.0.dbamp(),
      waitTime,
      waitTimeMin = 40.0,
      waitTimeMax = 95.0,
      droneWaterTransitionTime = 12.0,
      transitionStaggerTime = 0.5 * droneWaterTransitionTime;

    this.droneChannel.playfx(this.droneFilterPatch);

    {

      this.waterChannel.play(this.waterPatch);
      this.droneChannel.play(this.dronePatch);

      this.waterChannel.level = 0.0;
      this.droneChannel.level = 0.0;

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
