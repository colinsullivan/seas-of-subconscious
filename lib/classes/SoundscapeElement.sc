SoundscapeElement : Object {
  var <>outChannel,
    /**
     *  A unique symbol to use for this element.  Override in child
     *  classes.
     **/
    <>key,
    <>soundscape,
    <>instr,
    <>transitionTime,
    <>onTimeMax,
    <>onTimeMin,
    <>offTimeMin,
    <>offTimeMax;

  init {
    arg args;

    this.key = args[\key];
    this.soundscape = args[\soundscape];

    this.outChannel = MixerChannel.new(
      this.key,
      Server.default,
      2,
      2,
      1.0,
      outbus: this.soundscape.masterChannel
    );
    this.outChannel.guiUpdateTime = 0.05;
  }

  create_next_patch {
  
  }

  /**
   *  Called from soundscape when all buffers have been loaded.
   **/
  prepare_to_play {
  }

  play {
    var onTime,
      offTime;
    
    {

      while({ true }, {

        this.instr = this.create_next_patch();
        this.outChannel.play(this.instr);

        offTime = rrand(this.offTimeMin, this.offTimeMax);
        offTime.wait();

        this.instr.set(\gate, 1);
        this.transitionTime.wait();
        
        onTime = rrand(this.onTimeMin, this.onTimeMax) - (2.0 * this.transitionTime);
        onTime.wait();

        this.instr.set(\gate, 0);
        this.transitionTime.wait();

        this.instr.stop();
      
      });
    
    }.fork();
  
  }
}
