FogHornElement : SoundscapeElement {
  var <>outLevelMin,
    <>outLevelMax;
  
  init {
    arg args;

    super.init(args);
    
    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -10.0.dbamp()
    );

    this.outLevelMin = -38.0.dbamp();
    this.outLevelMax = -24.0.dbamp();

    this.offTimeMin = 50.0;
    this.offTimeMax = 120.0;
    /*this.offTimeMin = 5.0;*/
    /*this.offTimeMax = 5.0;*/

    this.transitionTime = 0.1;
  }

  create_next_patch {
    var buf;

    super.create_next_patch();

    "preparing fog horn".postln();

    buf = this.soundscape.bufs[\fogHornBuf];

    this.onTimeMin = buf.duration;
    this.onTimeMax = buf.duration;

    this.outChannel.level = rrand(this.outLevelMin, this.outLevelMax);

    ^Patch("cs.sfx.PlayBuf", (
      buf: buf,
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime,
      gate: KrNumberEditor.new(0, \gate.asSpec())
    ));
  }
}
