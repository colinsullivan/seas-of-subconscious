FogHornElement : SoundscapeElement {
  var <>outLevelMin,
    <>outLevelMax,
    <>bufKeys;
  
  init {
    arg args;

    super.init(args);
    
    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -10.0.dbamp()
    );

    this.bufKeys = [\fogHornBuf, \fogHorn02Buf];

    this.outLevelMin = -32.0.dbamp();
    this.outLevelMax = -24.0.dbamp();

    this.offTimeMin = 30.0;
    this.offTimeMax = 80.0;
    /*this.offTimeMin = 5.0;*/
    /*this.offTimeMax = 5.0;*/

    this.transitionTime = 0.1;
  }

  create_next_patch {
    var bufKey,
      bufSection,
      buf;

    super.create_next_patch();
    
    bufKey = this.bufKeys.choose();
    buf = this.soundscape.bufs[bufKey];

    ("preparing fog horn" ++ bufKey).postln();

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