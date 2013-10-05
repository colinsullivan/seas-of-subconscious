/**
 *  @file       FogHornElement.sc
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Foghorn action.
 **/
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

    this.outLevelMin = -18.0.dbamp();
    this.outLevelMax = -20.0.dbamp();

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
    buf = this.bufManager.bufs[bufKey];

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
