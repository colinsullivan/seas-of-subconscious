SoundsOfSubconsciousSeas : Object {

  var <>masterChannel,
    <>droneChannel,
    <>waterChannel,
    <>animalsChannel,
    <>channels,
    <>instrs,
    <>bufs,
    <>animalBufs,
    <>animalTransitionTime,
    <>creakingFloorboardMarkers,
    <>nextCreakStartTime,
    <>nextCreakEndTime,
    <>creakAttackTime,
    <>creakReleaseTime,
    <>creakingChannel;

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
    this.creakingChannel = create_channel_to_mixer.value(\creakingChannel, 0.6);
    
    this.bufs = (
      splashingWaterBuf: 0,
      warblerBuf: 0,
      gullsBuf: 0,
      creakingFloorboardBuf: 0
    );

    this.animalBufs = [\warblerBuf, \gullsBuf];
    this.animalTransitionTime = 2.0;

    this.creakingFloorboardMarkers = [
      0.0,
      3.5,
      6.5,
      9.7,
      12.7,
      15.5,
      18.7,
      22.1,
      25.4,
      29.2,
      32.2,
      35.5,
      38.9,
      41.9,
      44.8,
      48.3,
      51.6,
      54.6,
      57.5
    ];
    this.nextCreakStartTime = nil;
    this.nextCreakEndTime = nil;
    this.creakAttackTime = 0.1;
    this.creakReleaseTime = 0.1;

    this.instrs = (
      drone: 0,
      water: 0,
      animal: 0,
      creakingFloorboard: 0
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
    "prepare_next_animal".postln;
    this.instrs[\animal] = Patch("cs.sfx.PlayBuf", (
      buf: this.bufs[this.animalBufs.choose()],
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      attackTime: this.animalTransitionTime,
      releaseTime: this.animalTransitionTime
    ));

    this.animalsChannel.play(this.instrs[\animal]);
  }

  prepare_next_creak {

    var nextCreakIndex;
        
    "prepare_next_creak".postln;

    nextCreakIndex = (this.creakingFloorboardMarkers.size - 2).rand();
    this.nextCreakStartTime = this.creakingFloorboardMarkers[nextCreakIndex];
    this.nextCreakEndTime = this.creakingFloorboardMarkers[nextCreakIndex + 1];
    
    this.instrs[\creakingFloorboard] = Patch("cs.sfx.PlayBuf", (
      buf: this.bufs[\creakingFloorboardBuf],
      attackTime: this.creakAttackTime,
      releaseTime: this.creakReleaseTime,
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      startTime: this.nextCreakStartTime,
      convertToStereo: 1
    ));
    
    this.creakingChannel.play(this.instrs[\creakingFloorboard]);
  }

  start_creaks {
    var creakTime,
      waitTime,
      waitTimeMin = 5.0,
      waitTimeMax = 24.0;
    {

      while({ true }, {
        this.prepare_next_creak();

        waitTime = rrand(waitTimeMin, waitTimeMax);
        waitTime.wait();

        "play_creak".postln;
        this.instrs[\creakingFloorboard].set(\gate, 1);

        creakTime = this.nextCreakEndTime - this.nextCreakStartTime;
        creakTime.wait();

        this.instrs[\creakingFloorboard].set(\gate, 0);
        this.creakReleaseTime.wait();
      
      });
    
    }.fork();
  }
  
  start_soundscape {

    this.start_ambience();
    this.start_animals();
    this.start_creaks();
  
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

        this.prepare_next_animal();
        offTime = rrand(offTimeMin, offTimeMax);
        offTime.wait();

        "play_animal".postln;
        this.instrs[\animal].set(\gate, 1);
        this.animalTransitionTime.wait();
        
        onTime = rrand(onTimeMin, onTimeMax);
        onTime.wait();

        this.instrs[\animal].set(\gate, 0);
        this.animalTransitionTime.wait();
      
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
