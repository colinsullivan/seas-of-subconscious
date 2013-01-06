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
    
    {
      this.waterChannel.levelTo(1.0, 2.0);

      2.0.wait();

      5.0.wait();

      this.waterChannel.levelTo(0.0, 2.0); 

      2.0.wait();

    }.fork();
  }

  setup_drone {
    this.instrs[\drone] = Patch("SeasOfSubconsciousDrone", (
      freq: 110
    ));

  }

  start_drone {
    this.droneChannel.play(this.instrs[\drone]);
    
    {

      1.0.wait();
      
      this.droneChannel.levelTo(1.0, 2.0);

      4.0.wait();

      this.droneChannel.levelTo(0.0, 2.0);

      2.0.wait();

    }.fork();
  
  }
  
}
