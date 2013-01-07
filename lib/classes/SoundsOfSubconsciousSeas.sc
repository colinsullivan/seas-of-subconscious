SoundsOfSubconsciousSeas : Object {

  var <>masterChannel,
    <>droneChannel,
    <>waterChannel,
    <>animalsChannel,
    <>channels,
    <>instrs,
    <>bufs,
    <>animalBufs,
    <>animalTransitionTime;

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
    
    this.animalsChannel = create_channel_to_mixer.value(\animalsChannel, 1.0);
    
    this.bufs = (
      splashingWaterBuf: 0,
      warblerBuf: 0,
      gullsBuf: 0
    );

    this.animalBufs = [\warblerBuf, \gullsBuf];
    this.animalTransitionTime = 2.0;

    this.instrs = (
      drone: 0,
      water: 0,
      animal: 0
    );
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
    this.prepare_next_animal();

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

  prepare_next_animal {
    this.instrs[\animal] = Patch("cs.sfx.PlayBuf", (
      buf: this.bufs[this.animalBufs.choose()],
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      attackTime: this.animalTransitionTime,
      releaseTime: this.animalTransitionTime
    ));
  }

  start_soundscape {

    this.start_ambience();
    this.start_animals();
  
  }

  start_animals {
    var onTime,
      onTimeMin = 5.0,
      onTimeMax = 12.0,
      offTime,
      offTimeMin = 10.0,
      offTimeMax = 20.0;
    {

      while({ true }, {

        "play".postln;
        
        this.animalsChannel.play(this.instrs[\animal]);

        1.0.wait();

        "gate on".postln;
        this.instrs[\animal].set(\gate, 1);

        this.animalTransitionTime.wait();
        
        onTime = rrand(onTimeMin, onTimeMax);
        "onTime:".postln;
        onTime.postln;
        onTime.wait();

        this.instrs[\animal].set(\gate, 0);

        this.animalTransitionTime.wait();

        this.prepare_next_animal();

        offTime = rrand(offTimeMin, offTimeMax);
        "offTime:".postln;
        offTime.postln;
        offTime.wait();
      
      });
    
    }.fork();
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
