/**
 *  @file       EntrySoundscape.sc
 *
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  A different soundscape as people are entering the seas of the
 *  subconscious.  Heard from outside, less eerie, but still mysterious.
 **/
EntrySoundscape : Soundscape {
  
  var <>reverbReturn;

  init_channels {
    super.init_channels();

    this.masterChannel.playfx(FxPatch("cs.fx.Notcher.notcher", (
      notchFreqs: [24000.0 ],
      notchrQs:   [0.1   ],
      notchdBs:   [-6.0  ],
      numChan: 2
    )));
    0.5.wait();
    this.reverbReturn = MixerChannel.new(
      nil,
      Server.default,
      2,
      2,
      1.0,
      outbus: this.masterChannel
    );
    0.5.wait();
    this.reverbReturn.playfx(FxPatch("cs.fx.Reverb.reverberator", (
      numChan: 2
    )));
    0.5.wait();

  }

  init_elements {
    super.init_elements();

    this.elements = (
      //ambience: AmbienceElement.new(),
      animals: AnimalsSoundscapeElement.new(),
      creaks: CreakSoundscapeElement.new(),
      //creepyBells: CreepyBellElement.new(),
      //dreams: DreamsSoundscapeElement.new(),
      melodic: MelodicSoundscapeElement.new(),
      fogHorn: FogHornElement.new(),
      shanties: ShantiesSoundscapeElement.new()
    );
  
  }

}
