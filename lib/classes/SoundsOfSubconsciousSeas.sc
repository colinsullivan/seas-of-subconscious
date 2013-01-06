SoundsOfSubconsciousSeas : Object {

  var <>masterChannel,
    <>bufs;

  init {

    this.masterChannel = MixerChannel.new(\masterChannel, Server.default, 2, 2, 1.0);
    this.bufs = (
      splashingWaterBuf: 0
    )
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
    "bufs_all_loaded".postln;
  }
  
}
