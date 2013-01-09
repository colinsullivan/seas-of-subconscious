SoundsOfSubconsciousSeas : Object {

  var <>masterChannel,
    <>droneChannel,
    <>waterChannel,
    <>channels,
    <>instrs,
    <>bufs,
    <>elements,
    <>creaks,
    <>animals;

  init {

    var create_channel_to_mixer, test;

    this.masterChannel = MixerChannel.new(\masterChannel, Server.default, 2, 2, 1.0);

    /*test = Patch("cs.fx.Test");

    "test.synthDef.numChannels:".postln;
    test.synthDef.numChannels.postln;

    this.masterChannel.playfx(test);*/

    create_channel_to_mixer = {
      arg chanKey, initialLevel;
      var chan = MixerChannel.new(
        chanKey,
        Server.default,
        2,
        2,
        initialLevel,
        outbus: this.masterChannel
      );
      chan.guiUpdateTime = 0.05;

      chan;
    };

    this.droneChannel = create_channel_to_mixer.value(\droneChannel, 0);
    
    this.waterChannel = create_channel_to_mixer.value(\waterChannel, 0);
    
    this.bufs = (
      splashingWaterBuf: 0,
      warblerBuf: 0,
      gullsBuf: 0,
      loonsBuf: 0,
      creakingFloorboardBuf: 0
    );


    this.instrs = (
      drone: 0,
      water: 0
    );

    this.animals = AnimalsSoundscapeElement.new();
    this.animals.init((
      soundscape: this,
      key: \animals
    ));

    this.creaks = CreakSoundscapeElement.new();
    this.creaks.init((
      soundscape: this,
      key: \creaks
    ));

  }

  load_buf {
    arg bufPath, bufKey;
    var me = this;

    Buffer.read(
      Server.default,
      bufPath,
      action: {
        arg buf;

        me.buf_loaded(bufKey, buf);
      };
    );
  
  }

  buf_loaded {
    arg bufKey, buf;

    this.bufs[bufKey] = buf;

    // if all bufs are not zero
    if (this.bufs.any({ arg item; item == 0; }) == false, {
      // finish loading
      this.bufs_all_loaded();
    });
  }

  bufs_all_loaded {

    "bufs_all_loaded".postln;

    this.setup_drone();
    this.setup_water();

    {
      2.0.wait();
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

    this.start_ambience();

    this.animals.play();
    this.creaks.play();
  
  }

  start_ambience {
    var waterLevelLow = 0.1,
      waterLevelHigh = 0.75,
      droneLevelLow = 0.1,
      droneLevelHigh = 0.75,
      waitTime,
      waitTimeMin = 20.0,
      waitTimeMax = 45.0,
      droneWaterTransitionTime = 10.0,
      transitionStaggerTime = 0.5 * droneWaterTransitionTime;

    {

      this.start_water();
      this.start_drone();

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
