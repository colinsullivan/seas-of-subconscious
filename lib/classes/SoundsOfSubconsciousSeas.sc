SoundsOfSubconsciousSeas : Object {

  var <>masterChannel,
    <>reverbReturn,
    <>channels,
    <>elements,
    <>bufs;

  init {

    var create_channel_to_mixer, test;

    this.masterChannel = MixerChannel.new(
      \masterChannel,
      Server.default,
      2,
      2,
      2.4
    );
    this.masterChannel.playfx(FxPatch("cs.fx.Notcher.notcher", (
      notchFreqs: [24000.0 ],
      notchrQs:   [0.1   ],
      notchdBs:   [-6.0  ],
      numChan: 2
    )));

    this.reverbReturn = MixerChannel.new(
      \reverbReturn,
      Server.default,
      2,
      2,
      1.0,
      outbus: this.masterChannel
    );
    this.reverbReturn.playfx(FxPatch("cs.fx.Reverb.reverberator", (
      numChan: 2
    )));

    this.bufs = (
      splashingWaterBuf: 0,
      warblerBuf: 0,
      gullsBuf: 0,
      loonsBuf: 0,
      creakingFloorboardBuf: 0,
      shipsBellBuf: 0,
      derbyshireRunningBuf: 0,
      avadhuta01Buf: 0,
      avadhuta02Buf: 0,
      descendingDissonantBuf: 0,
      triadsBuf: 0,
      baleinesBuf: 0,
      fogHornBuf: 0,
      fogHorn02Buf: 0,
      seaShanty01: 0,
      seaShanty02: 0,
      seaShanty03: 0,
      seaShanty04: 0
    );

    this.elements = (
      ambience: AmbienceElement.new(),
      animals: AnimalsSoundscapeElement.new(),
      creaks: CreakSoundscapeElement.new(),
      creepyBells: CreepyBellElement.new(),
      dreams: DreamsSoundscapeElement.new(),
      melodic: MelodicSoundscapeElement.new(),
      fogHorn: FogHornElement.new(),
      shanties: ShantiesSoundscapeElement.new()
    );

    // initialize all elements
    this.elements.keysValuesDo({
      arg key, element;

      element.init((
        soundscape: this,
        key: key
      ));
    });

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
    arg bufKey, buf, msg;

    this.bufs[bufKey] = buf;

    /*("loaded buf: " ++ bufKey).postln();*/

    // if all bufs are not zero
    if (this.bufs.any({ arg item; item == 0; }) == false, {
      // finish loading
      this.bufs_all_loaded();
    }, {
      msg = "bufs to load:";

      this.bufs.keysValuesDo({
        arg bufKey, bufValue;

        if (bufValue == 0, {
          msg = msg ++ bufKey ++ ", ";
        });
      });

      /*msg.postln();*/
    });
  }

  bufs_all_loaded {

    "bufs_all_loaded".postln;

    this.elements.do({
      arg element;

      element.prepare_to_play();
    });

    {
      2.0.wait();
      this.start_soundscape();
    }.fork();

  }

  start_soundscape {

    // play all elements
    this.elements.do({
      arg element;

      if (element != 0, {
        element.play();    
      });
    });
  
  }
}
